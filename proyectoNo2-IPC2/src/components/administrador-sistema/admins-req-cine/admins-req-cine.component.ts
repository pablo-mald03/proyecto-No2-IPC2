import { Component } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AdminsCineCardsComponent } from "../admins-cine-cards/admins-cine-cards.component";

@Component({
  selector: 'app-admins-req-cine',
  imports: [RouterLink, RouterLinkActive, AdminsCineCardsComponent, AdminsCineCardsComponent],
  templateUrl: './admins-req-cine.component.html',
  styleUrl: './admins-req-cine.component.scss'
})
export class AdminsReqCineComponent {

  administradores: Usuario[] = [];
  administradoresMostrados: Usuario[] = [];
  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;

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
        codigoRol: 'ROL-2'
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
        codigoRol: 'ROL-2'
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
        codigoRol: 'ROL-2'
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
        codigoRol: 'ROL-2'
      }
    ];

    this.cargarMasAdministradores();
  }


  cargarMasAdministradores(): void {
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
