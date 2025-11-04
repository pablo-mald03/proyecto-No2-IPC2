import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BilleteraDigital } from '../../models/usuarios/billetera-digital';
import { BilleteraDigitalService } from '../../services/usuarios-service/billetera-digital.service';
import { SaldoBilleteraDTO } from '../../models/usuarios/saldo-billetera-dto';
import { UserLoggedDTO } from '../../models/usuarios/user-logged-dto';
import { Popup } from '../../shared/popup/popup';
import { SharedPopupComponent } from '../pop-ups/shared-popup.component/shared-popup.component';

@Component({
  selector: 'app-billetera-digital',
  imports: [NgIf, FormsModule, ReactiveFormsModule, CommonModule, SharedPopupComponent],
  templateUrl: './billetera-digital.component.html',
  styleUrl: './billetera-digital.component.scss',
  providers: [Popup],
})
export class BilleteraDigitalComponent implements OnInit {

  formRecarga!: FormGroup;
  mensaje: string = '';
  cargando = false;
  idUsuario: string = '';
  saldoActual: number | null = null;


  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  constructor(
    private fb: FormBuilder,
    private router: Router,
    private billeteraService: BilleteraDigitalService,
    private popUp: Popup,
  ) { }

  //Inicializa el formulario
  ngOnInit(): void {
    // Crear el formulario reactivo
    this.formRecarga = this.fb.group({
      saldo: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d+(\.\d{1,2})?$/), // números y hasta 2 decimales
          Validators.min(0.01)
        ]
      ]
    });

    this.popUp.popup$.subscribe(data => {
      // Se actualiza el contenido del popup
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

    const usuarioStr = localStorage.getItem('angularUserCinema');
    if (!usuarioStr) {
      this.router.navigateByUrl('/login');
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;
    this.idUsuario = usuario.id;

    this.obtenerSaldoActual();
  }

  //Metodo que carga instantaneamente el saldo del usuario
  obtenerSaldoActual(): void {


    this.billeteraService.obtenerSaldo(this.idUsuario).subscribe({
      next: (response: SaldoBilleteraDTO) => {

        this.saldoActual = response.saldo;

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }

  //Metodo que muestra el mensaje de error
  mostrarError(error: any): void {

    let mensaje = 'Ocurrió un error';

    if (error.error && error.error.mensaje) {
      mensaje = error.error.mensaje;
    } else if (error.message) {
      mensaje = error.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

  }

  //Metodo en el que se obtiene el saldo
  get saldo() {
    return this.formRecarga.get('saldo');
  }

  //Metodo que ejecuta el submit del service
  onSubmit(): void {
    if (this.formRecarga.invalid) {
      this.formRecarga.markAllAsTouched();
      return;
    }

    const usuarioStr = localStorage.getItem('angularUserCinema');
    if (!usuarioStr) {
      this.router.navigate(['/login']);
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;
    const request: BilleteraDigital = {
      saldo: Number(this.saldo?.value),
      idUsuario: usuario.id
    };

    this.cargando = true;
    this.mensaje = '';

    this.billeteraService.recargarSaldo(request).subscribe({
      next: (response: any) => {
        let mensaje = 'Recarga realizada con exito';
        this.popUp.mostrarPopup({ mensaje, tipo: 'exito' });
        this.cargando = false;
        this.obtenerSaldoActual();
        this.formRecarga.reset();
      },
      error: (err: any) => {
        this.mostrarError(err);
        this.cargando = false;
      }
    });
  }

}
