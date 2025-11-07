import { Component, Input } from '@angular/core';
import { Cine } from '../../../models/cines/cine';
import { CommonModule, NgClass } from '@angular/common';
import { CineDTO } from '../../../models/cines/cine-dto';

@Component({
  selector: 'app-cines-asociados-cards',
  imports: [NgClass,CommonModule],
  templateUrl: './cines-asociados-cards.component.html',
  styleUrl: './cines-asociados-cards.component.scss'
})
export class CinesAsociadosCardsComponent {

  @Input() cine!: CineDTO;


  cambiarCosto(codigo: string):void{
    
  }
  editarCine(codigo: string

):void{

  }
}
