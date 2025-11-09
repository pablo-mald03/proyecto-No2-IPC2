import { CommonModule, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { AnuncioPublicidadDTO } from '../../../models/anuncios/anuncio-publicidad-dto';

@Component({
  selector: 'app-anuncios-cards-pagina',
  imports: [NgIf, CommonModule,],
  templateUrl: './anuncios-cards-pagina.component.html',
  styleUrl: './anuncios-cards-pagina.component.scss'
})
export class AnunciosCardsPaginaComponent {


  @Input() anuncio!: AnuncioPublicidadDTO;

  constructor(private sanitizer: DomSanitizer) { }

  //Metodo encargado de poder reconocer el anuncio tipo base64
  getImagenBase64(): SafeUrl | null {
    if (!this.esImagen() || !this.anuncio.foto) return null;
    const base64 = `data:image/png;base64,${this.anuncio.foto}`;
    return this.sanitizer.bypassSecurityTrustUrl(base64);
  }

  //Metodo encargado de poder convertir los videos 
  getVideoUrl(): SafeResourceUrl {
    if (!this.esVideo() || !this.anuncio.url) return '';

    let url = this.anuncio.url;

    if (url.includes('watch?v=')) {
      const videoId = url.split('watch?v=')[1].split('&')[0];
      url = `https://www.youtube-nocookie.com/embed/${videoId}?autoplay=1&loop=1&playlist=${videoId}&mute=1&controls=0&showinfo=0&modestbranding=1`;
    } else {
      url = url.replace('youtube.com', 'youtube-nocookie.com');
    }

    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
  esTexto(): boolean {
    return this.anuncio.codigoTipo === 1;
  }

  esImagen(): boolean {
    return this.anuncio.codigoTipo === 2;
  }

  esVideo(): boolean {
    return this.anuncio.codigoTipo === 3;
  }

  //Metodo encargado de obtener el tipo de anuncio que es
  getTipoAnuncio(codigo: number): string {
    switch (codigo) {
      case 1: return TipoAnuncioEnum.ANUNCIO_TEXTO;
      case 2: return TipoAnuncioEnum.IMAGEN_TEXTO;
      case 3: return TipoAnuncioEnum.VIDEO_TEXTO;
      default: return 'Desconocido';
    }
  }
}
