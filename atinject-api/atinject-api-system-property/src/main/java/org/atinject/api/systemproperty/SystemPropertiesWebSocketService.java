package org.atinject.api.systemproperty;

import java.util.Map.Entry;

import org.atinject.api.authorization.RequiresPermissions;
import org.atinject.api.systemproperty.dto.GetSystemPropertiesRequest;
import org.atinject.api.systemproperty.dto.GetSystemPropertiesResponse;
import org.atinject.api.systemproperty.dto.SystemProperty;
import org.atinject.core.tiers.WebSocketService;

@WebSocketService
public class SystemPropertiesWebSocketService {

    @RequiresPermissions(SystemPropertyPermissions.READ_SYSTEM_PROPERTIES)
    public GetSystemPropertiesResponse getSystemProperties(GetSystemPropertiesRequest request) {
        for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
            SystemProperty systemProperty = new SystemProperty()
                    .withKey(entry.getKey().toString())
                    .withValue(entry.getValue().toString());
        }
        return new GetSystemPropertiesResponse();
    }
}
