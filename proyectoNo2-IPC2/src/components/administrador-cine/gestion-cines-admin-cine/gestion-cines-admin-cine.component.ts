import { Component } from '@angular/core';

@Component({
  selector: 'app-gestion-cines-admin-cine',
  imports: [],
  templateUrl: './gestion-cines-admin-cine.component.html',
  styleUrl: './gestion-cines-admin-cine.component.scss'
})
export class GestionCinesAdminCineComponent {


  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  

    //Metodo que permite llamar al metodo que carga dinamicamente los registros
  mostrarMasCines(): void {

   /* if (this.todosCargados || this.anunciosMostrados.length === 0) {
      return;
    }

    this.cargarMasRegistros();*/
  } 
}
