package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.producer.UserOrderProducer;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;
    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;
    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;
    private final StoreService storeService;
    private final StoreConverter storeConverter;
    private final UserOrderProducer userOrderProducer;

    public UserOrderResponse userOrder(User user, UserOrderRequest body) {
        List<StoreMenuEntity> storeMenuEntityList = body.getStoreMenuIdList().stream()
                .map(storeMenuService::getStoreMenuWithThrow)
                .collect(Collectors.toList());

        UserOrderEntity userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);
        UserOrderEntity savedUserOrderEntity = userOrderService.order(userOrderEntity);
        storeMenuEntityList.stream()
                .map(it -> userOrderMenuConverter.toEntity(savedUserOrderEntity, it))
                .forEach(userOrderMenuService::order);
        // 비동기 주문 메시지 전송
        userOrderProducer.sendOrder(savedUserOrderEntity);

        return userOrderConverter.toResponse(savedUserOrderEntity);
    }

    public List<UserOrderDetailResponse> current(User user) {
        List<UserOrderEntity> userOrderEntityList = userOrderService.current(user.getId());
        return getUserOrderList(userOrderEntityList);
    }

    public List<UserOrderDetailResponse> history(User user) {
        List<UserOrderEntity> userOrderEntityList = userOrderService.history(user.getId());
        return getUserOrderList(userOrderEntityList);
    }

    private List<UserOrderDetailResponse> getUserOrderList(List<UserOrderEntity> userOrderEntityList) {
        return userOrderEntityList.stream()
                .map(it -> {
                    List<StoreMenuEntity> storeMenuEntityList = getOrderMenuList(it);
                    // 사용자가 주문한 스토어 TODO 리팩토링 필요
                    return getUserOrderDetailResponse(it, storeMenuEntityList);
                })
                .collect(Collectors.toList());
    }

    public UserOrderDetailResponse read(User user, Long orderId) {
        UserOrderEntity userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());
        List<StoreMenuEntity> storeMenuEntityList = getOrderMenuList(userOrderEntity);

        // 사용자가 주문한 스토어 TODO 리팩토링 필요
        return getUserOrderDetailResponse(userOrderEntity, storeMenuEntityList);
    }

    private List<StoreMenuEntity> getOrderMenuList(UserOrderEntity it) {
        List<UserOrderMenuEntity> userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
        return userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity ->
                        storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId()))
                .collect(Collectors.toList());
    }

    private UserOrderDetailResponse getUserOrderDetailResponse(UserOrderEntity it, List<StoreMenuEntity> storeMenuEntityList) {
        StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());
        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(it))
                .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build();
    }
}
