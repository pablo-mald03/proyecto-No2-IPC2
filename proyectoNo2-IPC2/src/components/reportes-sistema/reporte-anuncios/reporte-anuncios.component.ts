import { Component, OnInit } from '@angular/core';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { CardsReporteAnunciosComponent } from "../cards-reporte-anuncios/cards-reporte-anuncios.component";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Popup } from '../../../shared/popup/popup';
import { ReporteAnuncioDTO } from '../../../models/reportes/reporte-anuncio-dto';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { HttpResponse } from '@angular/common/http';
import { ReporteAnunciosService } from '../../../services/reportes-sistema-service/reporte-anuncios.service';
import { ExportarAnunciosCompradosService } from '../../../services/reportes-sistema-service/exportar-anuncios-comprados.service';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';

@Component({
  selector: 'app-reporte-anuncios',
  imports: [NgFor, CardsReporteAnunciosComponent, ReactiveFormsModule, NgClass, SharedPopupComponent, NgIf],
  templateUrl: './reporte-anuncios.component.html',
  styleUrl: './reporte-anuncios.component.scss',
  providers: [Popup],
})
export class ReporteAnunciosComponent implements OnInit {

  tipoAnuncioEnum = TipoAnuncioEnum;

  anuncios: ReporteAnuncioDTO[] = [];
  anunciosMostrados: ReporteAnuncioDTO[] = [];

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

  constructor(
    private formBuild: FormBuilder,
    private reporteAnunciosCompradosService: ReporteAnunciosService,
    private exportarReporteAnunciosCompradosService: ExportarAnunciosCompradosService,
    private popUp: Popup,
  ) { }

  //Inicializa el formulario de filtros
  ngOnInit(): void {
    this.filtrosForm = this.formBuild.group({
      tipoAnuncio: ['todos'],
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.indiceActual = 0;
    this.anunciosMostrados = [];
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

    let fileName = 'ReporteAnunciosComprados.pdf';
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

    if (this.anunciosMostrados.length === 0) {
      const mensaje = 'Genera primero los reportes para poder exportarlos';
      this.popUp.mostrarPopup({ mensaje, tipo: 'info' });
      return;
    }


    if (this.filtrosForm.invalid || this.fechaInvalida) {
      return;
    }

    const { tipoAnuncio, fechaInicio, fechaFin } = this.filtrosForm.value;



    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';


    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }


    if (!this.estaFiltrado) {


      this.exportarReporteAnunciosCompradosService.exportarReportesAnunciosSinFiltro(inicioISO, finISO, this.indiceActual, 0).subscribe({
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

      if (!tipoAnuncio || tipoAnuncio.trim() === '') {
        const mensaje = 'No puedes dejar vacío el campo de tipo de usuario vacio';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }

      if (!tipoAnuncio || tipoAnuncio.trim() === 'todos') {
        const mensaje = 'Si desea filtrar selecciona un tipo especifico';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }

      this.exportarReporteAnunciosCompradosService.exportarReportesAnunciosConFiltro(inicioISO, finISO, this.indiceActual, 0, tipoAnuncio).subscribe({
        next: (response: HttpResponse<Blob>) => {

          this.descargarReporte(response);
        },
        error: (error: any) => {

          this.mostrarError(error);

        }
      });



    }

  }



  //Inicializa los tipos de anuncios que hay
  tipoAnuncioOptions = Object.keys(TipoAnuncioEnum)
    .filter(val => ['ANUNCIO_TEXTO', 'IMAGEN_TEXTO', 'VIDEO_TEXTO'].includes(val))
    .map(key => ({
      key: key,
      value: TipoAnuncioEnum[key as keyof typeof TipoAnuncioEnum]
    }));



  //Verifica si la fehca es valida 
  get fechaInvalida(): boolean {
    const inicio = this.filtrosForm.get('fechaInicio')?.value;
    const fin = this.filtrosForm.get('fechaFin')?.value;

    if (!inicio || !fin) return false;
    return new Date(inicio) > new Date(fin);
  }

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
      this.limpiarFiltros();
    }


    this.reporteAnunciosCompradosService.cantidadReportesSinFiltro(inicioISO, finISO).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.anunciosMostrados = [];
        this.todosCargados = false;
        this.estaFiltrado = false;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


  }

  //Metodo que sirve para mostrar mas reportes dinamicamente
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

      console.log('si entra al no filtro');

      this.reporteAnunciosCompradosService.reportesAnunciosCompradosSinFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
        next: (response: ReporteAnuncioDTO[]) => {

          if (!response || response.length === 0) {
            this.todosCargados = true;
            return;
          }

          this.anunciosMostrados.push(...response);
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

      const { tipoAnuncio } = this.filtrosForm.value;

      console.log('si entra al filtro');

      if (!tipoAnuncio || tipoAnuncio.trim() === '') {
        const mensaje = 'No puedes dejar vacío el campo de tipo de anuncio vacio';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }

      if (!tipoAnuncio || tipoAnuncio.trim() === 'todos') {
        const mensaje = 'Si desea filtrar selecciona un tipo especifico';
        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
        return;
      }


      this.reporteAnunciosCompradosService.reportesAnunciosCompradosConFiltro(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual, tipoAnuncio).subscribe({
        next: (response: ReporteAnuncioDTO[]) => {

          if (!response || response.length === 0) {
            this.todosCargados = true;
            return;
          }

          this.anunciosMostrados.push(...response);
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



  //Permite filtrar por tipo de anuncio 
  filtrar(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) {
      return;
    }

    const { tipoAnuncio, fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';


    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    if (!tipoAnuncio || tipoAnuncio.trim() === '') {
      const mensaje = 'No puedes dejar vacío el campo de tipo de usuario vacio';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    if (!tipoAnuncio || tipoAnuncio.trim() === 'todos') {
      const mensaje = 'Si desea filtrar selecciona un tipo especifico';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }


    this.reporteAnunciosCompradosService.cantidadReportesConFiltro(inicioISO, finISO, tipoAnuncio).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.anunciosMostrados = [];
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


  //Permite limpiar los filtros 
  limpiarFiltros(): void {
    this.filtrosForm.patchValue({
      tipoAnuncio: 'todos',
      fechaInicio: null,
      fechaFin: null
    });

    this.estaFiltrado = false;

    this.generarReporte();
  }

  //Metodo que sirve para limpiar las fechas de reporte
  limpiarFechas(): void {
    this.filtrosForm.reset();

    this.filtrosForm.patchValue({
      tipoAnuncio: 'todos',
      fechaInicio: null,
      fechaFin: null
    });

    this.indiceActual = 0;
    this.anunciosMostrados = [];
    this.todosCargados = true;
    this.estaFiltrado = false;
  }

}
