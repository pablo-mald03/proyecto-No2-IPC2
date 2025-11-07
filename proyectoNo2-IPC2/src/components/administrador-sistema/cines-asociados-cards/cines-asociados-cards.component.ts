import { Component, Input } from '@angular/core';
import { Cine } from '../../../models/cines/cine';
import { CommonModule, NgClass } from '@angular/common';

@Component({
  selector: 'app-cines-asociados-cards',
  imports: [NgClass,CommonModule],
  templateUrl: './cines-asociados-cards.component.html',
  styleUrl: './cines-asociados-cards.component.scss'
})
export class CinesAsociadosCardsComponent {

  @Input() cine!: Cine;
}
