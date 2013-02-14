package org.atinject.tool;

import org.atinject.api.user.dto.GetUserRequest;

public class DTOToJavascript
{
    public static void main(String[] args){
        Class<GetUserRequest> requestClass = GetUserRequest.class;

        // function BaseDTO () {
        // }

        // BaseDTO.prototype.getClass() = function() {
        //     return this.@class;
        // };
        
        // function BaseWebSocketRequest() {
        //     this.@class = "org.atinject.api.user.GetUserRequest";
        // }
        
        // BaseWebSocketRequest.prototype = new BaseDTO();
        
        // BaseWebSocketRequest.prototype.getRequestId() = function() {
        //     return this.requestId;
        // };
        
        // BaseWebSocketRequest.prototype.setRequestId() = function(requestId) {
        //     this.requestId = requestId;
        //     return this;
        // };
        
        // function GetUserRequest() {
        // }
        
        // GetUserRequest.prototype = new BaseWebSocketRequest();
        // ...
    }
}
