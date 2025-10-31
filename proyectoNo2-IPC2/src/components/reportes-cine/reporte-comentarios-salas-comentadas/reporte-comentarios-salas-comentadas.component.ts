import { NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ReporteSalasComentadasDTO } from '../../../models/reportes-cine/reporte-salas-comentarios-dto';
import { ReporteSalasComentadasCardsComponent } from "../reporte-salas-comentadas-cards/reporte-salas-comentadas-cards.component";
import { ReporteComentariosSalaService } from '../../../services/reportes-cine-service/reporte-comentarios-sala.sercive';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';

@Component({
  selector: 'app-reporte-comentarios-salas-comentadas',
  imports: [NgClass, ReactiveFormsModule, ReporteSalasComentadasCardsComponent],
  templateUrl: './reporte-comentarios-salas-comentadas.component.html',
  styleUrl: './reporte-comentarios-salas-comentadas.component.scss'
})
export class ReporteComentariosSalasComentadasComponent implements OnInit {


  //Apartado de atributos que sirven para exponer los dto
  reporteSalas: ReporteSalasComentadasDTO[] = [];
  reportesMostrados: ReporteSalasComentadasDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;


  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  filtroSalaForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private formBuildSala: FormBuilder,
    private reporteComentariosService: ReporteComentariosSalaService,

  ) { }


  //Inicializa los atributos en nulos
  ngOnInit(): void {

    //Se teneran los filtros reactivos
    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.filtroSalaForm = this.formBuildSala.group({
      idSala: [null]
    });

    this.indiceActual = 0;
    this.reportesMostrados = [];
    this.todosCargados = true;


  }


  //Metodo que sirve para mandar a exportar el reporte
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
  generarReporte(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : null;
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : null;

    if (!inicioISO || !finISO) return;


    // Paso 1: obtener cantidad total
    this.reporteComentariosService.cantidadReportesSinFiltro(inicioISO, finISO).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.reportesMostrados = [];
        this.todosCargados = false;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (err) => console.error('Error obteniendo cantidad total', err)
    });



  }


  //Metodo que sirve para ir cargando dinamicamente los reportes de salas de cine comentadas
  mostrarMasReportes(): void {

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    if (!fechaInicio || !fechaFin) return;

    const inicioISO = new Date(fechaInicio).toISOString().split('T')[0];
    const finISO = new Date(fechaFin).toISOString().split('T')[0];

    this.cargarMasReportes(inicioISO, finISO);

  }


  //Metodo que sirve para cargar mas y no mostrar todos de golpe
  cargarMasReportes(fechaInicioISO: string, fechaFinISO: string): void {

    if (this.todosCargados) return;

    this.reporteComentariosService.reportesSalasComentadasSinFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: ReporteSalasComentadasDTO[]) => {

        if (!response || response.length === 0) {
          this.todosCargados = true;
          return;
        }

        this.reportesMostrados.push(...response);
        this.indiceActual += response.length;

        if (this.indiceActual >= this.totalReportes) {
          this.todosCargados = true;
        }


      },
      error: (error: any) => {

        let mensaje = 'Ocurrió un error';

        if (error.error && error.error.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.message) {
          mensaje = error.message;
        }

      }
    });
  }



  //Metodo que sirve para limpiar las fechas de reporte
  limpiarFechas(): void {
    this.filtrosForm.reset();
  }


  //Metodo que sirve para limpiar la sala de filtro
  limpiarSala(): void {
    this.filtroSalaForm.patchValue({
      idSala: ''
    });
  }

  //Metodo que sirve para filtrar por sala
  filtrarSala(): void {

    const { idSala } = this.filtroSalaForm.value;

    if (idSala == null || idSala == '') {
      return;
    }

    //Pendiente hacer la query
    console.log('trilin');

  }
}
