package ch.hsr.apparch.kitchendeviceservice.repository;


import ch.hsr.apparch.kitchendeviceservice.model.KitchenDevice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface KitchenDeviceRepository extends PagingAndSortingRepository<KitchenDevice, Long> {

}
