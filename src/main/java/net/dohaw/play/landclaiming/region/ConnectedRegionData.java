package net.dohaw.play.landclaiming.region;

import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectedRegionData extends RegionData{

    private List<SingleRegionData> connectedData;

    public ConnectedRegionData(UUID ownerUUID, List<SingleRegionData> connectedData, RegionType type, RegionDescription desc){
        this.connectedData = connectedData;
        this.type = type;
        this.desc = desc;
        this.ownerUUID = ownerUUID;
    }

    public List<SingleRegionData> getConnectedData(){
        return connectedData;
    }

    public List<Chunk> getChunks(){
        List<Chunk> chunks = new ArrayList<>();
        for(SingleRegionData srd : connectedData){
            chunks.add(srd.getChunk());
        }
        return chunks;
    }

    public void addData(SingleRegionData singleRegionData){
        connectedData.add(singleRegionData);
    }

    public void removeData(SingleRegionData singleRegionData){
        connectedData.remove(singleRegionData);
    }

    public static ConnectedRegionData join(List<ConnectedRegionData> crdList){
        if(crdList.size() > 1){
            List<SingleRegionData> srdList = new ArrayList<>();
            for(ConnectedRegionData crd : crdList){
                List<SingleRegionData> connectedRegions = crd.getConnectedData();
                srdList.addAll(connectedRegions);
            }
            ConnectedRegionData firstData = crdList.get(0);
            return new ConnectedRegionData(firstData.ownerUUID, firstData.connectedData, firstData.type, firstData.desc);
        }else{
            return crdList.get(0);
        }
    }

}
