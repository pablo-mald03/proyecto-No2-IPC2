import { CommonModule, NgClass, NgFor, NgIf } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";
import { ConfiguracionAnuncioDTO } from '../../../models/anuncios/configuracion-anuncio-dto';
import { ConfiguracionAnunciosService } from '../../../services/anuncios-service/configuracion-anuncios.service';
import { ConfiguracionAnunciosCardsComponent } from "../configuracion-anuncios-cards/configuracion-anuncios-cards.component";
import { forkJoin } from 'rxjs';
import { VigenciaAnuncio } from '../../../models/anuncios/vigencia-anuncio';
import { VigenciaAnunciosCardsComponent } from "../vigencia-anuncios-cards/vigencia-anuncios-cards.component";
import { VigenciaAnunciosService } from '../../../services/anuncios-service/vigencia-anuncios.service';
import { FullscreenModalComponent } from '../../../shared/fullscreen-modal/fullscreen-modal.component';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { Router } from '@angular/router';
import { ComprarAnuncioUsuarioService } from '../../../services/anuncios-service/comprar-anuncio-usuario.service';

@Component({
  selector: 'app-comprar-anuncio',
  imports: [ReactiveFormsModule, CommonModule, NgFor, SharedPopupComponent, NgIf, ConfiguracionAnunciosCardsComponent, FullscreenModalComponent, VigenciaAnunciosCardsComponent],
  templateUrl: './comprar-anuncio.component.html',
  styleUrl: './comprar-anuncio.component.scss',
  providers: [Popup],
})
export class ComprarAnuncioComponent implements OnInit {

  usuarioAnunciante!: UserLoggedDTO;

  anuncioForm!: FormGroup;
  pagoForm!: FormGroup;

  //informacion de formularios
  configuracionesAnuncios: ConfiguracionAnuncioDTO[] = [];
  vigenciasAnuncios: VigenciaAnuncio[] = [];

  imagenFile: File | null = null;

  //Atributos del popup
  mostrarPopup = false;
  mensajePopup = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  mostrarSegundoFormulario = false;

  formDataAnuncio!: FormData;

  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Router para navegar
  router = inject(Router);

  constructor(
    private fb: FormBuilder,
    private popUp: Popup,
    private configuracionAnunciosService: ConfiguracionAnunciosService,
    private vigenciaAnunciosService: VigenciaAnunciosService,
    private comprarAnuncioService: ComprarAnuncioUsuarioService,
  ) { }

  ngOnInit(): void {

    const usuarioStr = localStorage.getItem("angularUserCinema");

    if (!usuarioStr) {

      this.router.navigateByUrl('/login');
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

    this.usuarioAnunciante = usuario;

    this.urlRedireccion = `/menu-anunciante/anuncios/comprados/${this.usuarioAnunciante.id}`;

    this.cargarCostosAnuncios();

    // Formulario del anuncio
    this.anuncioForm = this.fb.group({
      codigo: [''],
      estado: [true],
      nombre: ['', Validators.required],
      caducacion: [false],
      fechaExpiracion: ['', Validators.required],
      fechaCompra: [new Date()],
      url: [''],
      texto: ['', Validators.required],
      foto: [''],
      codigoTipo: [null, Validators.required],  // ahora vendrá de configuracionesAnuncios
      codigoTarifa: [null, Validators.required], // vendrá del backend
      idUsuario: [''],
    });

    // Formulario de pago
    this.pagoForm = this.fb.group({
      metodo: [{ value: 'TARJETA', disabled: true }, Validators.required],
      monto: [{ value: 0, disabled: true }]
    });

    // Si cambia cualquier dato del formulario principal, resetea el formulario de pago
    this.anuncioForm.valueChanges.subscribe(() => {
      if (this.mostrarSegundoFormulario) {
        this.mostrarSegundoFormulario = false;
        this.pagoForm.reset({ metodo: '', monto: 0 });
      }
    });

    // Suscripción al popup
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

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }

  // Cargar configuraciones desde el backend
  cargarCostosAnuncios(): void {

    this.configuracionAnunciosService.listadoConfiguraciones().subscribe({
      next: (response) => this.configuracionesAnuncios = response,
      error: (e) => this.mostrarError(e)
    });

    this.vigenciaAnunciosService.listadoVigencias().subscribe({
      next: (response) => this.vigenciasAnuncios = response,
      error: (e) => this.mostrarError(e)
    });
  }

  // Selección de imagen
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.imagenFile = file;

      const reader = new FileReader();
      reader.onload = () => {
        this.anuncioForm.patchValue({ foto: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  }

  // Enviar primer formulario
  submit() {
    if (this.anuncioForm.invalid) {
      this.anuncioForm.markAllAsTouched();
      return;
    }

    this.formDataAnuncio = new FormData();

    const formValue = this.anuncioForm.value;

    this.formDataAnuncio.append('nombre', formValue.nombre);
    this.formDataAnuncio.append('codigoTipo', formValue.codigoTipo);
    this.formDataAnuncio.append('texto', formValue.texto);
    this.formDataAnuncio.append('url', formValue.url);
    this.formDataAnuncio.append('fechaCompra', formValue.fechaExpiracion);
    this.formDataAnuncio.append('tipoTarifa', formValue.codigoTarifa);

    if (this.imagenFile) {
      this.formDataAnuncio.append('foto', this.imagenFile);
    }

    this.calcularTotalListo(formValue.codigoTipo, formValue.codigoTarifa);

    this.mostrarSegundoFormulario = true;
  }

  limpiar() {
    this.anuncioForm.reset({
      estado: true,
      fechaCompra: new Date()
    });
    this.imagenFile = null;
    this.mostrarSegundoFormulario = false;
  }

  mostrarError(error: any) {
    const mensaje = error?.error?.mensaje ?? error?.message ?? 'Ocurrió un error';
    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
  }

  // Confirmar pago
  confirmarPago() {
    if (this.pagoForm.invalid) {
      this.pagoForm.markAllAsTouched();
      return;
    }

    this.formDataAnuncio.append('monto', this.pagoForm.getRawValue().monto);

    this.formDataAnuncio.append('idUsuario', this.usuarioAnunciante.id);



    this.comprarAnuncioService.comprarAnuncio(this.formDataAnuncio).subscribe({
      next: () => {

        this.limpiar();
        this.abrirModal('Se ha registrado tu anuncio en el sistema', 'exito');
      },
      error: (err) => {
        this.mostrarError(err);
      }
    });
  }

  // Calcular total real desde backend
  calcularTotalListo(idConfig: number, idTarifa: number): void {

    forkJoin({
      config: this.configuracionAnunciosService.configuracionAnuncioCodigo(idConfig),
      tarifa: this.vigenciaAnunciosService.vigenciaAnuncioCodigo(idTarifa)
    })
      .subscribe({
        next: ({ config, tarifa }) => {
          const total = config.precio + tarifa.precio;
          this.pagoForm.patchValue({ monto: total });
        },
        error: (e) => this.mostrarError(e)
      });
  }
}
