import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CinesAsociadosDTO } from '../../../models/cines/cines-asociados-dto';
import { CommonModule, NgFor } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { CineInformacionService } from '../../../services/cine-service/cine-informacion.service';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-crear-admin-cine',
  imports: [FormsModule, ReactiveFormsModule, RouterLink, RouterLinkActive, CommonModule, NgFor, SharedPopupComponent],
  templateUrl: './crear-admin-cine.component.html',
  styleUrl: './crear-admin-cine.component.scss',
  providers: [Popup],
})
export class CrearAdminCineComponent implements OnInit {


  nuevoRegistroForm!: FormGroup;
  fotoSeleccionada: string | null = null;

  //Arreglo de cines asociados
  cinesDisponibles: CinesAsociadosDTO[] = [];


  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  //Cines que se envian via llave valor
  cinesSeleccionados: CinesAsociadosDTO[] = [];

  constructor(
    private fb: FormBuilder,
    private popUp: Popup,
    private cineInformacionService: CineInformacionService,
  ) { }

  ngOnInit(): void {

    this.indiceActual = 0;
    this.cinesDisponibles = [];
    this.todosCargados = true;


    this.nuevoRegistroForm = this.fb.group({
      nombre: ['', Validators.required],
      identificacion: ['', Validators.required],
      id: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(5)]],
      confirmpassword: ['', [Validators.required, Validators.minLength(5)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{8}$/)]],
      pais: ['', Validators.required],
      foto: [null],
      codigosCine: [[], this.tieneUnCine.bind(this)]
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



    this.cineInformacionService.cantidadRegistros().subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {

        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cinesDisponibles = [];
        this.todosCargados = false;

        this.cargarMasCines();
      },
      error: (error: any) => {

        this.mostrarError(error);

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

  //Metodo que permite ir cargando dinamicamente los cines
  cargarMasCines(): void {

    if (this.todosCargados) return;

    this.cineInformacionService.listadoRegistros(this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: CinesAsociadosDTO[]) => {
        this.ampliarResultados(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


  }

  //Metodo encargado de ir ampliando dinamicamente los cines registrados o asociados en el sistema
  ampliarResultados(response: CinesAsociadosDTO[]): void {

    if (!response || response.length === 0) {
      this.todosCargados = true;
      return;
    }

    this.cinesDisponibles.push(...response);
    this.indiceActual += response.length;

    if (this.indiceActual >= this.totalReportes) {
      this.todosCargados = true;
    }

  }

  //Metodo que permite verificar que tenga al menos un cine
  tieneUnCine(control: FormControl) {
    const value = control.value;
    if (Array.isArray(value) && value.length > 0) {
      return null;
    }
    return { sinCines: true };
  }


  // Toggle (agregar o quitar) cines seleccionados
  toggleSeleccion(cine: CinesAsociadosDTO): void {
    const index = this.cinesSeleccionados.findIndex(c => c.codigo === cine.codigo);
    if (index >= 0) {
      this.cinesSeleccionados.splice(index, 1);
    } else {
      this.cinesSeleccionados.push(cine);
    }

    const codigos = this.cinesSeleccionados.map(c => c.codigo);
    this.nuevoRegistroForm.patchValue({ codigosCine: codigos });
  }


  // Saber si una card ya está seleccionada
  estaSeleccionado(cine: CinesAsociadosDTO): boolean {
    return this.cinesSeleccionados.some(c => c.codigo === cine.codigo);
  }

  //Cargar imagen y convertirla a Base64
  seleccionHecha(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.fotoSeleccionada = reader.result as string;
        this.nuevoRegistroForm.patchValue({ foto: this.fotoSeleccionada });
      };
      reader.readAsDataURL(file);
    }
  }


  //Metodo que permite reiniciar formulario y selección
  reiniciar(): void {
    this.nuevoRegistroForm.reset();
    this.cinesSeleccionados = [];
    this.fotoSeleccionada = null;
  }

  //Metodo que envia el formulario
  submit(): void {
    if (this.nuevoRegistroForm.valid) {
      const formData = new FormData();

      Object.entries(this.nuevoRegistroForm.value).forEach(([key, value]) => {
        if (key === 'codigosCine') {
          formData.append(key, JSON.stringify(value));
        } else {
          formData.append(key, value as any);
        }
      });

      console.log('Datos enviados:', this.nuevoRegistroForm.value);
      // this.adminService.crearAdministrador(formData).subscribe(...)
    } else {
      this.nuevoRegistroForm.markAllAsTouched();
    }
  }

}
