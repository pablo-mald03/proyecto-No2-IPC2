import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BilleteraDigital } from '../../models/usuarios/billetera-digital';
import { BilleteraDigitalService } from '../../services/usuarios-service/billetera-digital.service';
import { SaldoBilleteraDTO } from '../../models/usuarios/saldo-billetera-dto';
import { UserLoggedDTO } from '../../models/usuarios/user-logged-dto';

@Component({
  selector: 'app-billetera-digital',
  imports: [NgIf, FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './billetera-digital.component.html',
  styleUrl: './billetera-digital.component.scss'
})
export class BilleteraDigitalComponent implements OnInit {

  formRecarga!: FormGroup;
  mensaje: string = '';
  cargando = false;
  idUsuario: string = '';
  saldoActual: number | null = null;


  constructor(
    private fb: FormBuilder,
    private router: Router,
    private billeteraService: BilleteraDigitalService,
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

    // === Recuperar usuario del localStorage ===
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

        /*let mensaje = 'Ocurrió un error';

        if (error.error && error.error.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.message) {
          mensaje = error.message;
        }

        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });*/

      }
    });

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

    const usuario = JSON.parse(usuarioStr);
    const request: BilleteraDigital = {
      saldo: Number(this.saldo?.value),
      idUsuario: usuario.idUsuario
    };

    this.cargando = true;
    this.mensaje = '';

    /*this.http.post('/api/billetera/recargar', request).subscribe({
      next: () => {
        this.mensaje = 'Recarga realizada con éxito.';
        this.cargando = false;
        this.formRecarga.reset();
      },
      error: (err) => {
        console.error(err);
        this.cargando = false;
      }
    });*/
  }

}
