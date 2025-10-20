import { Component } from '@angular/core';
import { SalaMasComenadaDTO } from '../../../models/reportes/sala-mas-comentada-dto';
import { SalasComentadasCardsComponent } from "../salas-comentadas-cards/salas-comentadas-cards.component";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-reporte-salas-comentadas',
  imports: [SalasComentadasCardsComponent, NgClass, ReactiveFormsModule],
  templateUrl: './reporte-salas-comentadas.component.html',
  styleUrl: './reporte-salas-comentadas.component.scss'
})
export class ReporteSalasComentadasComponent {


  reporteSalaComentada: SalaMasComenadaDTO[] = [];
  reportesMostrados: SalaMasComenadaDTO[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder

  ) { }


  ngOnInit(): void {

    //Se teneran los filtros reactivos
    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

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

    //Metodo get que sirve para validar si la fecha inicial antecede a la final
  get fechaInvalida(): boolean {
    const inicio = this.filtrosForm.get('fechaInicio')?.value;
    const fin = this.filtrosForm.get('fechaFin')?.value;
    if (!inicio || !fin) return false;
    return new Date(inicio) > new Date(fin);
  }

  //Metodo que sirve para validar las fechas
  validarFechas() {
    const { fechaInicio, fechaFin } = this.filtrosForm.value;
    if (fechaInicio && fechaFin && new Date(fechaInicio) > new Date(fechaFin)) {
      this.filtrosForm.setErrors({ fechaInvalida: true });
    } else {
      this.filtrosForm.setErrors(null);
    }
  }

  //Metodo que sirve para filtrar
  filtrar(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString() : null;
    const finISO = fechaFin ? new Date(fechaFin).toISOString() : null;

    //Pendiente hacer la query
    

  }

  limpiarFechas(): void {
    this.filtrosForm.reset();
  }

}
