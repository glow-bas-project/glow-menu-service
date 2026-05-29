package com.glow.menu.application.api;

import com.glow.menu.application.api.model.CreateMenuItemRequest;
import com.glow.menu.application.api.model.FindMenuItemIdsRequest;
import com.glow.menu.application.api.model.FindMenuItemIdsResponse;
import com.glow.menu.application.api.model.MaterialiseMenuItemsByIdsRequest;
import com.glow.menu.application.api.model.UpdateMenuItemRequest;
import com.glow.menu.application.model.MenuItemDto;
import com.glow.menu.application.services.MenuItemService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/menu-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuController {

    private final MenuItemService service;

    public MenuController(MenuItemService service) {
        this.service = service;
    }

    @POST
    public RestResponse<MenuItemDto> createMenuItem(CreateMenuItemRequest request) {
        return RestResponse.status(RestResponse.Status.CREATED, service.createMenuItem(request));
    }

    @GET
    @Path("/{id}")
    public RestResponse<MenuItemDto> findById(@PathParam("id") String id) {
        return RestResponse.ok(service.findById(id));
    }

    @PUT
    @Path("/{id}")
    public RestResponse<MenuItemDto> updateMenuItem(@PathParam("id") String id, UpdateMenuItemRequest request) {
        return RestResponse.ok(service.updateMenuItem(id, request));
    }

    @GET
    @Path("/restaurant/{restaurantId}")
    public RestResponse<List<MenuItemDto>> findByRestaurantId(@PathParam("restaurantId") String restaurantId) {
        return RestResponse.ok(service.findByRestaurantId(restaurantId));
    }

    @POST
    @Path("/find")
    public RestResponse<FindMenuItemIdsResponse> findMenuItemIds(FindMenuItemIdsRequest request) {
        var result = service.findMenuItemIds(request.size(), request.page(), request.restaurantId());

        return RestResponse.ok(new FindMenuItemIdsResponse(
            result.result(),
            result.nextPage(),
            !result.lastPage()));
    }

    @POST
    @Path("/materialise")
    public RestResponse<List<MenuItemDto>> materialiseMenuItems(MaterialiseMenuItemsByIdsRequest request) {
        return RestResponse.ok(service.materialise(request.ids()));
    }

    @DELETE
    @Path("/{id}")
    public RestResponse<Void> deleteById(@PathParam("id") String id) {
        boolean deleted = service.deleteMenuItemById(id);
        return deleted ? RestResponse.noContent() : RestResponse.status(RestResponse.Status.NOT_FOUND);
    }
}
