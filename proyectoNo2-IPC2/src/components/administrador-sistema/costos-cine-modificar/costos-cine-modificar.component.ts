import { Component, inject, OnInit } from '@angular/core';
import { CostoCineDTO } from '../../../models/cines/costo-cine-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, NgIf } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { FullscreenModalComponent } from '../../../shared/fullscreen-modal/fullscreen-modal.component';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { ActivatedRoute, Router } from '@angular/router';
import { CostosCineService } from '../../../services/cine-service/costos-cine-service';
import { CostoModificacionDTO } from '../../../models/cines/costo-modificacion.dto';

@Component({
  selector: 'app-costos-cine-modificar',
  imports: [ReactiveFormsModule, CommonModule, NgIf, FullscreenModalComponent, SharedPopupComponent],
  templateUrl: './costos-cine-modificar.component.html',
  styleUrl: './costos-cine-modificar.component.scss',
  providers: [Popup]
})
export class CostosCineModificarComponent implements OnInit {


  codigoCine!: string;

  costoForm!: FormGroup;

  costos: CostoModificacionDTO[] = [];



  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;



  constructor(
    private fb: FormBuilder,
    private popUp: Popup,
    private router: ActivatedRoute,
    private costosCineService: CostosCineService,
    private routerBack: Router,
  ) { }

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }


  //Metodo que carga al iniciar el service y permite visualizar el historial de modificaciones
  ngOnInit(): void {

    this.codigoCine = this.router.snapshot.params['codigo'];


    this.costoForm = this.fb.group({
      costo: [null, [Validators.required, Validators.min(1)]],
      fechaModificacion: [null, Validators.required]
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

    this.cargarDatosHistorial();

  }

  //Metodo utilizado para cargar o recargar los datos 
  cargarDatosHistorial() {

    this.costosCineService.listadoCostos(this.codigoCine).subscribe({
      next: (response: CostoModificacionDTO[]) => {

        this.costos = response;

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });
  }

  //Metodo utilizado para registrar el nuevo costo de cine
  registrarCosto() {
    if (this.costoForm.invalid) return;

    const fecha = this.costoForm.value.fechaModificacion;

    const nuevoCosto: CostoCineDTO = {
      costo: Number(this.costoForm.value.costo),
      fechaModificacion: fecha,
      codigoCine: this.codigoCine
    };


    this.costosCineService.registrarNuevoCosto(nuevoCosto).subscribe({
      next: () => {

        this.limpiarDatos();
        this.cargarDatosHistorial();
        this.abrirModal('Se ha modificado correctamente el costo del cine', 'exito');
      },
      error: (err) => {
        this.mostrarError(err);
      }
    });
  }

  //Metodo que permite limpiar los datos
  limpiarDatos() {
    this.costoForm.reset({
      costo: 0
    });
  }


  //Metodo util para mostrar errors
  mostrarError(error: any): void {

    let mensaje = 'Ocurrió un error';

    if (error.error && error.error.mensaje) {
      mensaje = error.error.mensaje;
    } else if (error.message) {
      mensaje = error.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
  }

  //Metodo que permite regresar
  regresar() {

    this.routerBack.navigateByUrl('/menu-admin-sistema/cines');
  }
}
