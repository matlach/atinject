package org.atinject.api.usertopology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.CDI;
import org.atinject.core.cdi.Named;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class UserTopologyService {

    @Inject @Named("user")
    private DistributedCache<UUID, UserEntity> userCache;
    
    @PostConstruct
    public void initialize(){
        // ensure we're a topology aware cluster
        if ( ! (userCache.getRpcManager().getAddress() instanceof TopologyAwareAddress))
        {
            throw new RuntimeException("must be in a topology aware cluster");
        }
        if (userCache.getDistributionManager() == null){
            throw new RuntimeException("must be a distributed cache");
        }
    }
    
    public TopologyAwareAddress getUserKeyPrimaryLocation(UUID userId){
        return (TopologyAwareAddress) userCache.getDistributionManager().getPrimaryLocation(userId);
    }
    
    public List<UserEntity> getAllUserByMachineId(String machineId) {
        Map<UUID, UserEntity> mappedUsers = userCache.performMapReduce(new UserIdMachineIdMapper(machineId), new UserReducer());
        return new ArrayList<>(mappedUsers.values());
    }
    
    public static class UserIdMachineIdMapper implements Mapper<UUID, UserEntity, UUID, UserEntity> {
        private static final long serialVersionUID = 1L;

        private String machineId;
        
        public UserIdMachineIdMapper(String machineId) {
            this.machineId = machineId;
        }
        
        @Override
        public void map(UUID key, UserEntity value, Collector<UUID, UserEntity> collector) {
            UserTopologyService userTopologyService = CDI.select(UserTopologyService.class).get();
            TopologyAwareAddress topologyAwareAddress = userTopologyService.getUserKeyPrimaryLocation(key);
            if (topologyAwareAddress.getMachineId().equals(machineId)) {
                collector.emit(key, value);
            }
        }
    }
    
    public static class UserReducer implements Reducer<UUID, UserEntity> {
        private static final long serialVersionUID = 1L;
        
        @Override
        public UserEntity reduce(UUID reducedKey, Iterator<UserEntity> iter) {
            return iter.next();
        }
    }
}
