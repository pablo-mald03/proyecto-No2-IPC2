import { Component, inject, OnInit } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';
import { AdminsSistemaCardComponent } from "../admins-sistema-card/admins-sistema-card.component";
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-admins-req-sistema',
  imports: [AdminsSistemaCardComponent, RouterLink, RouterLinkActive],
  templateUrl: './admins-req-sistema.component.html',
  styleUrl: './admins-req-sistema.component.scss'
})
export class AdminsReqSistemaComponent implements OnInit{

  administradores: Usuario[] = [];
  administradoresMostrados: Usuario[] = [];
  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;

  router = inject(Router);

  ngOnInit(): void {

    this.administradores = [
      {
        id: 'carlos-01',
        correo: 'admin1@cineapp.com',
        nombre: 'Carlos López',
        foto: '',
        password: '',
        telefono: '+502 5555-1234',
        pais: 'Guatemala',
        identificacion: '123456789',
        codigoRol: 'ROL-1'
      },
      {
        id: 'mariap-02',
        correo: 'admin2@cineapp.com',
        nombre: 'María Pérez',
        foto: '',
        password: '',
        telefono: '+502 5555-5678',
        pais: 'Guatemala',
        identificacion: '987654321',
        codigoRol: 'ROL-1'
      },
      {
        id: 'mariap-02',
        correo: 'admin2@cineapp.com',
        nombre: 'María Pérez',
        foto: '',
        password: '',
        telefono: '+502 5555-5678',
        pais: 'Guatemala',
        identificacion: '987654321',
        codigoRol: 'ROL-1'
      },
      {
        id: 'mariap-02',
        correo: 'admin2@cineapp.com',
        nombre: 'María Pérez',
        foto: '',
        password: '',
        telefono: '+502 5555-5678',
        pais: 'Guatemala',
        identificacion: '987654321',
        codigoRol: 'ROL-1'
      }
    ];

    this.cargarMasUsuarios();
  }

  volver() {
    this.router.navigateByUrl('/menu-admin-sistema/usuarios')

  }

  cargarMasUsuarios(): void {
    const siguienteBloque = this.administradores.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.administradoresMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.administradores.length) {
      this.todosCargados = true;
    }
  }

}
