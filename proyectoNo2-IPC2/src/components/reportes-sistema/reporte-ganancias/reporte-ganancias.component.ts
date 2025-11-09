import { Component, OnInit } from '@angular/core';
import { GananciasSistemaDTO } from '../../../models/reportes/ganancias-sistema-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, NgClass, NgIf } from '@angular/common';
import { ReporteGanaciasCardsComponent } from "../reporte-ganacias-cards/reporte-ganacias-cards.component";
import { Popup } from '../../../shared/popup/popup';
import { ExportarGananciasSistemaService } from '../../../services/reportes-sistema-service/exportar-ganancias-sistema.service';
import { GananciasSistemaService } from '../../../services/reportes-sistema-service/reporte-ganancias-sistema.service';
import { HttpResponse } from '@angular/common/http';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-reporte-ganancias',
  imports: [ReactiveFormsModule, NgClass, ReporteGanaciasCardsComponent, SharedPopupComponent, NgIf, CommonModule],
  templateUrl: './reporte-ganancias.component.html',
  styleUrl: './reporte-ganancias.component.scss',
  providers: [Popup],
})
export class ReporteGananciasComponent implements OnInit {

  //Apartado de atributos que sirven para exponer los dto
  reportesMostrados: GananciasSistemaDTO | null = null;


  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private popUp: Popup,
    private exportarReporteGananciasService: ExportarGananciasSistemaService,
    private reporteGananciasService: GananciasSistemaService,

  ) { }


  ngOnInit(): void {

    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

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

    let fileName = 'ReporteGanancias.pdf';
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



  //Metodo que sirve para mandar a exportar el reporte
  exportarReporte() {


    if (this.reportesMostrados == null) {
      const mensaje = 'Genera primero los reportes para poder exportarlos';
      this.popUp.mostrarPopup({ mensaje, tipo: 'info' });
      return;
    }


    if (this.filtrosForm.invalid || this.fechaInvalida) {
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



    this.exportarReporteGananciasService.exportarReporteGanancias(inicioISO, finISO).subscribe({
      next: (response: HttpResponse<Blob>) => {

        this.descargarReporte(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }

  //metodo encargado para generar el reporte
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



    this.reporteGananciasService.reportesGananciasSistema(inicioISO, finISO).subscribe({
      next: (response: GananciasSistemaDTO) => {

        this.reportesMostrados = response;


      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


  }

  //Verifica si la fehca es valida 
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



  //Metodo que sirve para limpiar las fechas de reporte
  limpiarFechas(): void {
    this.filtrosForm.reset();

    this.filtrosForm.patchValue({
      fechaInicio: null,
      fechaFin: null
    });

    this.reportesMostrados = null;
  }

}
