import { Component } from '@angular/core';
import { SalaMasComenadaDTO } from '../../../models/reportes/sala-mas-comentada-dto';
import { SalasComentadasCardsComponent } from "../salas-comentadas-cards/salas-comentadas-cards.component";

@Component({
  selector: 'app-reporte-salas-comentadas',
  imports: [SalasComentadasCardsComponent],
  templateUrl: './reporte-salas-comentadas.component.html',
  styleUrl: './reporte-salas-comentadas.component.scss'
})
export class ReporteSalasComentadasComponent {


  reporteSalaComentada: SalaMasComenadaDTO[] = [];
  reportesMostrados: SalaMasComenadaDTO[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {

    this.reporteSalaComentada = [
      {
        codigo: 'S001',
        nombre: 'Sala Premium 3D',
        filas: 12,
        columnas: 20,
        ubicacion: 'Nivel 2, ala norte',
        comentarios: [
          { idUsuario: 'U001', contenido: 'Excelente experiencia, la imagen es muy nítida.', fechaPosteo: new Date('2025-10-10') },
          { idUsuario: 'U002', contenido: 'Muy cómodos los asientos.', fechaPosteo: new Date('2025-10-12') }
        ]
      },
    ];



    this.cargarMasReportes();
  }


  //Apartado de metodos para gestionar los filtros
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteSalaComentada.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteSalaComentada.length) {
      this.todosCargados = true;
    }
  }

  exportarReporte() {


  }

}
