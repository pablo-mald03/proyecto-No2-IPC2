import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BilleteraCineDTO } from '../../../models/admins-cine/billetera-cine-dto';
import { CommonModule, NgIf } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { ActivatedRoute, Router } from '@angular/router';
import { BilleteraCineService } from '../../../services/admin-cine-service/billetera-cine.service';
import { FullscreenModalComponent } from "../../../shared/fullscreen-modal/fullscreen-modal.component";
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-recargar-billetera-cine',
  imports: [ReactiveFormsModule, CommonModule, NgIf, FullscreenModalComponent, SharedPopupComponent],
  templateUrl: './recargar-billetera-cine.component.html',
  styleUrl: './recargar-billetera-cine.component.scss',
  providers: [Popup],
})
export class RecargarBilleteraCineComponent implements OnInit {

  codigoCine!: string;

  billeteraForm!: FormGroup;

  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '/menu-admin-cine/cines/billetera';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  //atributo que alimenta al html 
  billetera!: BilleteraCineDTO;


  constructor(private formBuilder: FormBuilder,
    private popUp: Popup,
    private router: ActivatedRoute,
    private billeteraCineService: BilleteraCineService,
    private routerBack: Router,
  ) { }

  //Ejecuta la lectura de datos para poder especificar el monto
  ngOnInit(): void {
    // Obtener el código del cine desde la ruta
    this.codigoCine = this.router.snapshot.params['codigo'];

    // Llamar al servicio para obtener los datos de la billetera
    this.billeteraCineService.billeteraPorCodigo(this.codigoCine).subscribe({
      next: (response: BilleteraCineDTO) => {
        this.billetera = response;

        this.billeteraForm = this.formBuilder.group({
          saldo: ['', [Validators.required, Validators.min(1)]],
        });
      },
      error: (error: any) => {
        this.mostrarError(error);
      }
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

  // Método que ejecuta la recarga
  recargarSaldo(): void {
    if (this.billeteraForm.invalid) {
      this.billeteraForm.markAllAsTouched();
      return;
    }

    const montoRecarga = this.billeteraForm.value.saldo;

    const billeteraDto: BilleteraCineDTO = {

      codigoCine: this.billetera.codigoCine,
      saldo: montoRecarga,
      nombre: this.billetera.nombre,
    };

    this.billeteraCineService.recargarSaldo(billeteraDto).subscribe({
      next: () => {

        this.billeteraForm.reset();
        this.abrirModal('Transaccion realizada correctamente', 'exito');
      },
      error: (err) => {
        this.mostrarError(err);
      }
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

  // Redirige o regresa a la lista
  regresar() {
    this.routerBack.navigateByUrl('/menu-admin-cine/cines/billetera');
  }


  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }


}
