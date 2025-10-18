import { Component, inject } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';
import { AdminsSistemaCardComponent } from "../admins-sistema-card/admins-sistema-card.component";
import { Router } from '@angular/router';

@Component({
  selector: 'app-admins-req-sistema',
  imports: [AdminsSistemaCardComponent],
  templateUrl: './admins-req-sistema.component.html',
  styleUrl: './admins-req-sistema.component.scss'
})
export class AdminsReqSistemaComponent {

    administradoresMostrados: Usuario[] = [];
    todosCargados = false;

    router = inject(Router);

    ngOnInit(): void {

    this.administradoresMostrados = [
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
      }
    ];
  }

  volver(){
    this.router.navigateByUrl('/menu-admin-sistema/usuarios')

  }

}
