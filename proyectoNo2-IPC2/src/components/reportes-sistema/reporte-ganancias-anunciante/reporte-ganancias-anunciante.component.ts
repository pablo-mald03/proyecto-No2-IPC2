import { Component } from '@angular/core';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { GananciasAnuncianteCardsComponent } from "../ganancias-anunciante-cards/ganancias-anunciante-cards.component";
import { ReporteAnuncianteDTO } from '../../../models/reportes/anunciante-dto';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-reporte-ganancias-anunciante.component',
  imports: [GananciasAnuncianteCardsComponent, NgFor],
  templateUrl: './reporte-ganancias-anunciante.component.html',
  styleUrl: './reporte-ganancias-anunciante.component.scss'
})
export class ReporteGananciasAnuncianteComponent {

  reporteAnunciante: ReporteAnuncianteDTO[] = [];
  reportesMostrados: ReporteAnuncianteDTO[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {

    this.reporteAnunciante = [
      {
        id: 'USR001',
        nombre: 'Cinemark Guatemala',
        correo: 'cinemark@cine.com',
        total: 1200.5,
        anuncios: [
          {
            codigo: 'AN001',
            estado: true,
            nombre: 'Promo Octubre',
            fechaCompra: new Date('2025-10-01'),
            fechaExpiracion: new Date('2025-10-31'),
          },
          {
            codigo: 'AN002',
            estado: false,
            nombre: 'Promo Caducada',
            fechaCompra: new Date('2025-08-15'),
            fechaExpiracion: new Date('2025-09-15'),
          }
        ]
      },
      {
        id: 'USR002',
        nombre: 'Cinepolis Mixco',
        correo: 'mixco@cinepolis.com',
        total: 890.75,
        anuncios: []
      },
      {
        id: 'USR001',
        nombre: 'Cinemark Guatemala',
        correo: 'cinemark@cine.com',
        total: 1200.5,
        anuncios: [
          {
            codigo: 'AN001',
            estado: true,
            nombre: 'Promo Octubre',
            fechaCompra: new Date('2025-10-01'),
            fechaExpiracion: new Date('2025-10-31'),
          },
          {
            codigo: 'AN002',
            estado: false,
            nombre: 'Promo Caducada',
            fechaCompra: new Date('2025-08-15'),
            fechaExpiracion: new Date('2025-09-15'),
          }
        ]
      },
      {
        id: 'USR002',
        nombre: 'Cinepolis Mixco',
        correo: 'mixco@cinepolis.com',
        total: 890.75,
        anuncios: []
      }
    ];



    this.cargarMasReportes();
  }


  //Apartado de metodos para gestionar los filtros
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteAnunciante.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteAnunciante.length) {
      this.todosCargados = true;
    }
  }

  exportarReporte() {


  }



}
