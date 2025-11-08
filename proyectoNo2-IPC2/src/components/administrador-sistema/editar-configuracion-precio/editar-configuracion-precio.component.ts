import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfiguracionAnuncioDTO } from '../../../models/anuncios/configuracion-anuncio-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { Popup } from '../../../shared/popup/popup';
import { ConfiguracionAnunciosService } from '../../../services/anuncios-service/configuracion-anuncios.service';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";
import { FullscreenModalComponent } from "../../../shared/fullscreen-modal/fullscreen-modal.component";
import { CambiarPrecioDTO } from '../../../models/anuncios/cambiar-precio-dto';

@Component({
  selector: 'app-editar-configuracion-precio',
  imports: [ReactiveFormsModule, CommonModule, NgIf, SharedPopupComponent, FullscreenModalComponent],
  templateUrl: './editar-configuracion-precio.component.html',
  styleUrl: './editar-configuracion-precio.component.scss',
  providers: [Popup],
})
export class EditarConfiguracionPrecioComponent {

  //Codigo recibido por parametro
  codigoConfiguracion!: number;


  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '/menu-admin-sistema/costos/precios';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  //Guarda la instancia recibida de backend
  configuracion!: ConfiguracionAnuncioDTO;


  formConfig!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private popUp: Popup,
    private route: ActivatedRoute,
    private configuracionService: ConfiguracionAnunciosService,
    private routerBack: Router,
  ) {

  }

  //Metodo que sirve para cargar la informacion 
  ngOnInit(): void {

    this.codigoConfiguracion = this.route.snapshot.params['id'];

    this.configuracionService.configuracionAnuncioCodigo(this.codigoConfiguracion).subscribe({
      next: (response: ConfiguracionAnuncioDTO) => {

        this.configuracion = response;

        this.formConfig = this.fb.group({
          precio: [this.configuracion.precio, [Validators.required, Validators.min(10)]]
        });

      },
      error: (error: any) => this.mostrarError(error)
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
        setTimeout(() => this.mostrarPopup = false, data.duracion);
      }
    });
  }
  //Metodo que permite regresar 
  regresar():void{

    this.routerBack.navigateByUrl('/menu-admin-sistema/costos/precios');
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

  //metodo que ejecuta el submit al service
  guardarCambios() {
    if (this.formConfig.invalid) return;

    const cambioDto: CambiarPrecioDTO = {
      codigo: this.configuracion.codigo,
      precio: this.formConfig.value.precio
    };

    this.configuracionService.cambiarPrecio(cambioDto).subscribe({
      next: () => {

        this.formConfig.reset();

        this.abrirModal(
          `Se ha editado correctamente la configuración del tipo ${this.configuracion.tipo}`,
          'exito'
        );
      },
      error: (err) => this.mostrarError(err)
    });
  }

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }

}
