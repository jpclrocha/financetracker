package com.jope.financetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_roles")
@Data
public class Role {

    @Column(name = "role_id")
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public enum Values {
        BASIC(1L),
        ADMIN(2L);

        long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }

        public long getRoleId(){
            return this.roleId;
        }
    }
}
