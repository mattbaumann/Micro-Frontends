package ch.hsr.apparch.purchaselist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PurchaseListItem {

  @Id
  @GeneratedValue
  private long id;

  @Setter
  @Column(nullable = false)
  @NotBlank
  private String name;

  @Setter
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private PurchaseList list;

}
