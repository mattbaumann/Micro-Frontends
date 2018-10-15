package ch.hsr.apparch.purchaselist.repository;


import ch.hsr.apparch.purchaselist.model.KitchenDevice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface KitchenDeviceRepository extends PagingAndSortingRepository<KitchenDevice, Long> {

}
