import { Component } from '@angular/core';
import { GananciasSistemaDTO } from '../../../models/reportes/ganancias-sistema-dto';

@Component({
  selector: 'app-reporte-ganancias',
  imports: [],
  templateUrl: './reporte-ganancias.component.html',
  styleUrl: './reporte-ganancias.component.scss'
})
export class ReporteGananciasComponent {

  reporteGanancias: GananciasSistemaDTO[] = [];
  reportesMostrados: GananciasSistemaDTO[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {

    this.reporteGanancias = [
      /* {
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
       }*/
     ];
 

      this.cargarMasReportes();
  }


  //Apartado de metodos para gestionar los filtros
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteGanancias.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteGanancias.length) {
      this.todosCargados = true;
    }
  }

  exportarReporte() {


  }

}
