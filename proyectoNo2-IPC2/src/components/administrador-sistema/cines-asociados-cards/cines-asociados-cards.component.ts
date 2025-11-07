import { Component, Input } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { CineDTO } from '../../../models/cines/cine-dto';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-cines-asociados-cards',
  imports: [NgClass, CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './cines-asociados-cards.component.html',
  styleUrl: './cines-asociados-cards.component.scss'
})
export class CinesAsociadosCardsComponent {

  @Input() cine!: CineDTO;




  editarCine(codigo: string

  ): void {

  }
}
