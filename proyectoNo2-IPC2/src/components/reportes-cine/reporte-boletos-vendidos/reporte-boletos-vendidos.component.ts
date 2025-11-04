import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ReporteBoletosVendidosDTO } from '../../../models/reportes-cine/reporte-boletos-vendidos-dto';
import { BoletosVendidosCardsComponent } from "../boletos-vendidos-cards/boletos-vendidos-cards.component";
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { ReporteBoletosVendidosService } from '../../../services/reportes-cine-service/reporte-boletos-vendidos.service';
import { ExportarReporteBoletosVendidosService } from '../../../services/reportes-cine-service/exportar-reporte-boletos-vendidos.service';
import { HttpResponse } from '@angular/common/http';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';

@Component({
  selector: 'app-reporte-boletos-vendidos',
  imports: [NgClass, ReactiveFormsModule, BoletosVendidosCardsComponent, SharedPopupComponent, NgIf],
  templateUrl: './reporte-boletos-vendidos.component.html',
  styleUrl: './reporte-boletos-vendidos.component.scss',
  providers: [Popup],
})
export class ReporteBoletosVendidosComponent implements OnInit {

  //Apartado de atributos que sirven para exponer los dto
  reporteBoletos: ReporteBoletosVendidosDTO[] = [];
  reportesMostrados: ReporteBoletosVendidosDTO[] = [];

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
    private reporteBoletosVendidosService: ReporteBoletosVendidosService,
    private exportarReporteBoletosVendidosService: ExportarReporteBoletosVendidosService,
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



  //Metodo que sirve para ir cargando dinamicamente los reportes de boiletos vendidos
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


  //Metodo que sirve para mandar a exportar el reporte
  exportarReporte() {


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


      this.exportarReporteBoletosVendidosService.exportarReporteBoletosVendidosSinFiltro(inicioISO, finISO, this.indiceActual, 0).subscribe({
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

      const { idSala } = this.filtroSalaForm.value;

      if (!idSala || idSala.trim() === '') {
        const mensaje = 'No puedes dejar vacío el campo de id de la sala';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }

      this.exportarReporteBoletosVendidosService.exportarReporteBoletosVendidosConFiltro(inicioISO, finISO, this.indiceActual, 0, idSala).subscribe({
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

  //Metodo que sirve para generar el reporte en base a un intervalos especificado o no
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
      this.limpiarSala();
    }

    this.reporteBoletosVendidosService.cantidadReportesSinFiltro(inicioISO, finISO).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cantidadPorCarga = 2;
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

  //Metodo que sirve para limpiar las fechas de reporte de boletos vendidos
  limpiarFechas(): void {
    this.filtrosForm.reset();

    this.indiceActual = 0;
    this.cantidadPorCarga = 2;
    this.reportesMostrados = [];
    this.todosCargados = true;
    this.estaFiltrado = false;
  }


  //Metodo que sirve para limpiar la sala de filtro
  limpiarSala(): void {
    this.filtroSalaForm.patchValue({
      idSala: ''
    });

    this.estaFiltrado = false;

    this.generarReporte();
  }

  //Metodo que sirve para filtrar por sala el reporte de boletos vendidos
  filtrarSala(): void {

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    const { idSala } = this.filtroSalaForm.value;

    if (!idSala || idSala.trim() === '') {
      const mensaje = 'No puedes dejar vacío el campo de id de la sala';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    this.reporteBoletosVendidosService.cantidadReportesConFiltro(inicioISO, finISO, idSala).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cantidadPorCarga = 2;
        this.reportesMostrados = [];
        this.todosCargados = false;
        this.estaFiltrado = true;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }


  //Metodo que sirve para redireccionar cuando se exporta un reporte de boletos vendidos
  descargarReporte(respuesta: HttpResponse<Blob>) {
    const contentDisposition = respuesta.headers.get('Content-Disposition');

    let fileName = 'ReporteSalasGustadas.pdf';
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
  mostrarError(errorEncontrado: any): void {
    let mensaje = 'Ocurrió un error';

    if (errorEncontrado.error && errorEncontrado.error.mensaje) {
      mensaje = errorEncontrado.error.mensaje;
    } else if (errorEncontrado.message) {
      mensaje = errorEncontrado.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

  }

  //Metodo que permite ir amplieando el arreglo de datos acorde a los request
  ampliarResultados(response: ReporteBoletosVendidosDTO[]): void {

    if (!response || response.length === 0) {
      this.todosCargados = true;
      return;
    }

    this.reportesMostrados.push(...response);
    this.indiceActual += response.length;

    if (this.indiceActual >= this.totalReportes) {
      this.todosCargados = true;
    }

  }


  //Metodo que sirve para cargar mas dinamicamente y no mostrar todos de golpe
  cargarMasReportes(fechaInicioISO: string, fechaFinISO: string): void {

    if (this.todosCargados) return;

    if (!this.estaFiltrado) {

      if ((this.indiceActual + this.cantidadPorCarga) > 5) {
        this.indiceActual = 4;
        this.cantidadPorCarga = 1;
      }

      this.reporteBoletosVendidosService.reporteBoletosVendidosSinFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
        next: (response: ReporteBoletosVendidosDTO[]) => {
          this.ampliarResultados(response);

        },
        error: (error: any) => {

          this.mostrarError(error);

        }
      });


    } else {

      const { idSala } = this.filtroSalaForm.value;

      if (idSala == null || idSala == '') {
        this.estaFiltrado = false;
        return;
      }


      this.reporteBoletosVendidosService.reporteBoletosVendidosConFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual, idSala).subscribe({
        next: (response: ReporteBoletosVendidosDTO[]) => {

          this.ampliarResultados(response);
        },
        error: (error: any) => {

          this.mostrarError(error);
        }
      });


    }
  }


}
