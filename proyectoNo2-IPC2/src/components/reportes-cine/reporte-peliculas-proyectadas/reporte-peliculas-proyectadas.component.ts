import { Component, OnInit } from '@angular/core';
import { ReporteSalaPeliculaProyectadaDTO } from '../../../models/reportes-cine/reporte-sala-pelicula-proyectada-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { PeliculasProyectadasCardsComponent } from "../peliculas-proyectadas-cards/peliculas-proyectadas-cards.component";
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { ReportePeliculasSalaService } from '../../../services/reportes-cine-service/reporte-peliculas-sala.service';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';

@Component({
  selector: 'app-reporte-peliculas-proyectadas',
  imports: [NgClass, ReactiveFormsModule, PeliculasProyectadasCardsComponent, SharedPopupComponent, NgIf],
  templateUrl: './reporte-peliculas-proyectadas.component.html',
  styleUrl: './reporte-peliculas-proyectadas.component.scss',
  providers: [Popup]
})
export class ReportePeliculasProyectadasComponent implements OnInit {


  //Apartado de atributos que sirven para exponer los dto
  reportePelicula: ReporteSalaPeliculaProyectadaDTO[] = [];
  reportesMostrados: ReporteSalaPeliculaProyectadaDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  //Flag que sirve para saber si se ha filtrado
  estaFiltrado = false;


  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  filtroSalaForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private formBuildSala: FormBuilder,
    private reportePeliculasSalaService: ReportePeliculasSalaService,
    private popUp: Popup,

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
    this.estaFiltrado = false;


    this.popUp.popup$.subscribe(data => {
      this.mensajePopup = data.mensaje;
      this.tipoPopup = data.tipo;

      this.mostrarPopup = false;

      setTimeout(() => {
        this.popupKey++;
        this.mostrarPopup = true;
      }, 10);

      if (data.duracion) {
        setTimeout(() => {
          this.mostrarPopup = false;
        }, data.duracion);
      }
    });

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

    if (this.estaFiltrado) {
      this.limpiarSala();
    }


    this.reportePeliculasSalaService.cantidadReportesSinFiltro(inicioISO, finISO).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.reportesMostrados = [];
        this.todosCargados = false;
        this.estaFiltrado = false;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (error: any) => {

        let mensaje = 'Ocurrió un error';

        if (error.error && error.error.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.message) {
          mensaje = error.message;
        }

        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

      }
    });

  }

  //Metodo que sirve para ir cargando dinamicamente los reportes de peliculas en salas de cine
  mostrarMasReportes(): void {

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    if (!fechaInicio || !fechaFin) return;

    const inicioISO = new Date(fechaInicio).toISOString().split('T')[0];
    const finISO = new Date(fechaFin).toISOString().split('T')[0];

    this.cargarMasReportes(inicioISO, finISO);

  }

  //Metodo que sirve para limpiar las fechas de reporte de salas de cine
  limpiarFechas(): void {
    this.filtrosForm.reset();

    this.indiceActual = 0;
    this.reportesMostrados = [];
    this.todosCargados = true;
    this.estaFiltrado = false;
  }


  //Metodo que sirve para limpiar la sala que se filtro para visualizar las peliculas
  limpiarSala(): void {
    this.filtroSalaForm.patchValue({
      idSala: ''
    });

    this.estaFiltrado = false;

    this.generarReporte();
  }

  //Metodo que sirve para filtrar por sala
  filtrarSala(): void {

   const { fechaInicio, fechaFin } = this.filtrosForm.value;

    if (!fechaInicio || !fechaFin) return;

    const inicioISO = new Date(fechaInicio).toISOString().split('T')[0];
    const finISO = new Date(fechaFin).toISOString().split('T')[0];

    if (!inicioISO || !finISO) return;

    const { idSala } = this.filtroSalaForm.value;

    if (idSala == null || idSala == '') {
      return;
    }


    this.reportePeliculasSalaService.cantidadReportesConFiltro(inicioISO, finISO, idSala).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.reportesMostrados = [];
        this.todosCargados = false;
        this.estaFiltrado = true;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (error: any) => {

        let mensaje = 'Ocurrió un error';

        if (error.error && error.error.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.message) {
          mensaje = error.message;
        }

        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

      }
    });

  }


  //Metodo que sirve para cargar mas dinamicamente y no mostrar todos de golpe
  cargarMasReportes(fechaInicioISO: string, fechaFinISO: string): void {

    if (this.todosCargados) return;

    if (!this.estaFiltrado) {

      this.reportePeliculasSalaService.reportesSalasPeliculaSinFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
        next: (response: ReporteSalaPeliculaProyectadaDTO[]) => {

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

          this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

        }
      });


    } else {

      const { idSala } = this.filtroSalaForm.value;

      if (idSala == null || idSala == '') {
        this.estaFiltrado = false;
        return;
      }


      this.reportePeliculasSalaService.reportesSalasPeliculaConFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual, idSala).subscribe({
        next: (response: ReporteSalaPeliculaProyectadaDTO[]) => {

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

          this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

        }
      });


    }
  }

}
