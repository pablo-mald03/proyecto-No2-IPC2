import { Component, OnInit } from '@angular/core';
import { GananciasAnuncianteCardsComponent } from "../ganancias-anunciante-cards/ganancias-anunciante-cards.component";
import { ReporteAnuncianteDTO } from '../../../models/reportes/anunciante-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { ReporteGananciasAnuncianteService } from '../../../services/reportes-sistema-service/reporte-ganancias-anunciante.sercive';
import { ExportarGananciasAnuncianteService } from '../../../services/reportes-sistema-service/exportar-ganancias-anunciante.sercive';
import { HttpResponse } from '@angular/common/http';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';

@Component({
  selector: 'app-reporte-ganancias-anunciante.component',
  imports: [GananciasAnuncianteCardsComponent, ReactiveFormsModule, NgClass, SharedPopupComponent, NgIf],
  templateUrl: './reporte-ganancias-anunciante.component.html',
  styleUrl: './reporte-ganancias-anunciante.component.scss',
  providers: [Popup],
})
export class ReporteGananciasAnuncianteComponent implements OnInit {

  reporteAnunciante: ReporteAnuncianteDTO[] = [];
  reportesMostrados: ReporteAnuncianteDTO[] = [];

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

  //Atributo que permite filtrar por anunciante
  filtrosFormUsuario!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private formBuildUsuario: FormBuilder,
    private reporteGananciasAnuncianteService: ReporteGananciasAnuncianteService,
    private exportarReporteGananciasAnunciate: ExportarGananciasAnuncianteService,
    private popUp: Popup,

  ) { }

  //Inicializa todos los botones e interacciones que se tendran para poder solicitar las querys
  ngOnInit(): void {

    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.filtrosFormUsuario = this.formBuildUsuario.group({
      idUsuario: ['']
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


  //Metodo que sirve para redireccionar cuando se exporta un reporte
  descargarReporte(respuesta: HttpResponse<Blob>) {
    const contentDisposition = respuesta.headers.get('Content-Disposition');

    let fileName = 'ReporteSalasComentadas.pdf';
    if (contentDisposition) {
      const match = contentDisposition.match(/filename="(.+)"/);
      if (match && match[1]) {
        fileName = match[1];
      }
    }

    const url = window.URL.createObjectURL(respuesta.body!);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    a.click();

    window.URL.revokeObjectURL(url);

    this.popUp.mostrarPopup({ mensaje: 'Reporte generado correctamente', tipo: 'exito' });

  }

  //Metodo implementado para mostrar los mensajes de errores
  mostrarError(errorEncontrado: any) {
    let mensaje = 'Ocurrió un error';

    if (errorEncontrado.error && errorEncontrado.error.mensaje) {
      mensaje = errorEncontrado.error.mensaje;
    } else if (errorEncontrado.message) {
      mensaje = errorEncontrado.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

  }


  //Metodo que sirve para mandar a exportar el reporte acorde a lo que este configurado
  exportarReporte(): void {

    if (this.reportesMostrados.length === 0) {
      const mensaje = 'Genera primero los reportes para poder exportarlos';
      this.popUp.mostrarPopup({ mensaje, tipo: 'info' });
      return;
    }

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }


    if (!this.estaFiltrado) {


      this.exportarReporteGananciasAnunciate.exportarReportesGananciaAnunciantesSinFiltro(inicioISO, finISO, this.indiceActual, 0).subscribe({
        next: (response: HttpResponse<Blob>) => {

          this.descargarReporte(response);

        },
        error: (error: any) => {

          this.mostrarError(error);

        }
      });

      return;
    }


    if (this.estaFiltrado) {

      const { idUsuario } = this.filtrosFormUsuario.value;

      if (!idUsuario || idUsuario.trim() === '') {
        const mensaje = 'No puedes dejar vacío el campo de id de la sala';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }


      this.exportarReporteGananciasAnunciate.exportarReportesGananciaAnunciantesConFiltro(inicioISO, finISO, this.indiceActual, 0, idUsuario).subscribe({
        next: (response: HttpResponse<Blob>) => {

          this.descargarReporte(response);
        },
        error: (error: any) => {

          this.mostrarError(error);

        }
      });



    }

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


  //Metodo que sirve para generar el reporte
  generarReporte(): void {


    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    if (this.estaFiltrado) {
      this.limpiarUsuario();
    }


    this.reporteGananciasAnuncianteService.cantidadReportesSinFiltro(inicioISO, finISO).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.reportesMostrados = [];
        this.todosCargados = false;
        this.estaFiltrado = false;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


  }


  //Metodo que sirve para ir cargando dinamicamente los reportes de salas de cine comentadas
  mostrarMasReportes(): void {

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    this.cargarMasReportes(inicioISO, finISO);

  }

  //Metodo que sirve para cargar mas y no mostrar todos de golpe
  cargarMasReportes(fechaInicioISO: string, fechaFinISO: string): void {

    if (this.todosCargados) return;

    if (!this.estaFiltrado) {

      this.reporteGananciasAnuncianteService.reportesGananciasAuncianteSinFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
        next: (response: ReporteAnuncianteDTO[]) => {

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

          this.mostrarError(error);

        }
      });


    } else {
      const { idUsuario } = this.filtrosFormUsuario.value;

      if (!idUsuario || idUsuario.trim() === '') {
        const mensaje = 'No puedes dejar vacío el campo de id de la sala';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }


      this.reporteGananciasAnuncianteService.reportesGananciasAuncianteConFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual, idUsuario).subscribe({
        next: (response: ReporteAnuncianteDTO[]) => {

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

          this.mostrarError(error);

        }
      });


    }
  }


  //Metodo que sirve para filtrar
  //Metodo que sirve para filtrar por sala
  filtrarUsuario(): void {

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    const { idUsuario } = this.filtrosFormUsuario.value;

    if (!idUsuario || idUsuario.trim() === '') {
      const mensaje = 'No puedes dejar vacío el campo de id de la sala';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }


    this.reporteGananciasAnuncianteService.cantidadReportesConFiltro(inicioISO, finISO, idUsuario).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.reportesMostrados = [];
        this.todosCargados = false;
        this.estaFiltrado = true;

        this.cargarMasReportes(inicioISO, finISO);
        console.log('pito');
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


  //Metodo encargado para poder limpiar el id del usuario especificado 
  limpiarUsuario(): void {
    this.filtrosFormUsuario.patchValue({
      idUsuario: ''
    });

    this.estaFiltrado = false;

    this.generarReporte();
  }

  //Metodo que sirve para limpiar las fechas de reporte
  limpiarFechas(): void {
    this.filtrosForm.reset();

    this.indiceActual = 0;
    this.reportesMostrados = [];
    this.todosCargados = true;
    this.estaFiltrado = false;
  }



}
