import { Component, Input } from '@angular/core';
import { Cine } from '../../../models/cines/cine';
import { CommonModule } from '@angular/common';
import { CineInformacionDTO } from '../../../models/cines/cine-informacion-dto';

@Component({
  selector: 'app-cines-card',
  imports: [CommonModule],
  templateUrl: './cines-card.component.html',
  styleUrl: './cines-card.component.scss'
})
export class CinesCardComponent {

  @Input() cine!: CineInformacionDTO;


  private palette = [
    ['#ff7a7a', '#ffb86b'],
    ['#7afcff', '#6b8bff'],
    ['#b28cff', '#ff7ad1'],
    ['#6bffb8', '#4dd0e1'],
    ['#ffd86b', '#ff7a7a'],
    ['#a1ff7a', '#7ad1ff'],
    ['#ff9f7a', '#ff7a7a']
  ];

  getGradient(seed?: string): string {
    if (!seed) seed = Math.random().toString();
    // simple hash from seed
    let h = 0;
    for (let i = 0; i < seed.length; i++) {
      h = (h << 5) - h + seed.charCodeAt(i);
      h |= 0;
    }
    const idx = Math.abs(h) % this.palette.length;
    const next = (idx + 1) % this.palette.length;
    const c1 = this.palette[idx][0];
    const c2 = this.palette[idx][1];
    return `linear-gradient(135deg, ${c1} 0%, ${c2} 100%)`;
  }

  getInicial(name?: string) {
    return name ? name.trim().charAt(0).toUpperCase() : '';
  }


   visitarCine(codigo: string) {
    //PENDIENTE REEMPLAZAR CON UN GET
    console.log('Visitar cine con código:', codigo);

  }

}
