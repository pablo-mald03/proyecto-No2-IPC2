import { Component } from '@angular/core';
import { SalaMasGustadaDTO } from '../../../models/reportes/sala-mas-gustada-dto';
import { SalasGustadasCardsComponent } from "../salas-gustadas-cards/salas-gustadas-cards.component";

@Component({
  selector: 'app-reporte-salas-gustadas',
  imports: [SalasGustadasCardsComponent],
  templateUrl: './reporte-salas-gustadas.component.html',
  styleUrl: './reporte-salas-gustadas.component.scss'
})
export class ReporteSalasGustadasComponent {

  reporteSalaGustada: SalaMasGustadaDTO[] = [];
  reportesMostrados: SalaMasGustadaDTO[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {

    this.reporteSalaGustada = [
      {
        codigo: "S001",
        nombre: "Sala Premium 3D",
        filas: 12,
        columnas: 20,
        ubicacion: "Primer Nivel Interplaza",
        totalVentasBoleto: 450,
        usuarios: [
          {
            id: "juan-U001",
            identificacion: "pilininsanotr2",
            nombre: "Carlos López",
            correo: "pablomaldonado@mail.com"
          },
          {
            id: "U002",
            identificacion: "123456789",
            nombre: "Ana Pérez",
            correo: "ana.perez@mail.com"
          },
          {
            id: "U003",
            identificacion: "654987321",
            nombre: "Luis Martínez",
            correo: "luis.martinez@mail.com"
          }
        ]
      },
      {
        codigo: "S002",
        nombre: "Sala Adultos 2D",
        filas: 13,
        columnas: 15,
        ubicacion: "Segundo nivel walmart",
        totalVentasBoleto: 350,
        usuarios: [
          {
            id: "U001",
            identificacion: "987654321",
            nombre: "Carlos López",
            correo: "carlos.lopez@mail.com"
          },
          {
            id: "U002",
            identificacion: "123456789",
            nombre: "Ana Pérez",
            correo: "ana.perez@mail.com"
          },
          {
            id: "U003",
            identificacion: "654987321",
            nombre: "Luis Martínez",
            correo: "luis.martinez@mail.com"
          }
        ]
      }
    ];



    this.cargarMasReportes();
  }


  //Apartado de metodos para gestionar los filtros
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteSalaGustada.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteSalaGustada.length) {
      this.todosCargados = true;
    }
  }

  exportarReporte() {


  }

}
