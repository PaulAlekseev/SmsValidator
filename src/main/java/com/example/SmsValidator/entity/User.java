package com.example.SmsValidator.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;
  private String email;
  private String username;
  private String password;
  private Long telegramId;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<InvoiceEntity> invoices;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<ModemProviderSessionEntity> providerSessions;

  @Column(precision = 2)
  private double balance;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(cascade = CascadeType.DETACH, mappedBy = "reservedBy")
  private List<ModemEntity> reservedModems;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public List<ModemEntity> getReservedModems() {
    return reservedModems;
  }

  public void setReservedModems(List<ModemEntity> reservedModems) {
    this.reservedModems = reservedModems;
  }

  public List<InvoiceEntity> getInvoices() {
    return invoices;
  }

  public void setInvoices(List<InvoiceEntity> invoices) {
    this.invoices = invoices;
  }
}
