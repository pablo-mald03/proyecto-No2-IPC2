import { Component, Input } from '@angular/core';
import { BilleteraCineDTO } from '../../../models/admins-cine/billetera-cine-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-billetera-digital-card',
  imports: [CommonModule],
  templateUrl: './billetera-digital-card.component.html',
  styleUrl: './billetera-digital-card.component.scss'
})
export class BilleteraDigitalCardComponent {


  @Input() billetera!: BilleteraCineDTO;


  //metodo utilizado para poder recargar saldo
  recargarSaldo(codigo:string){



  }
}
