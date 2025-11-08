import { Component, Input } from '@angular/core';
import { AnuncioRegistradoDTO } from '../../../models/anuncios/anuncio-registrado-dto';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { CommonModule, DatePipe, NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'app-anuncios-registrados-sistema-cards',
  imports: [CommonModule, NgClass,NgIf,DatePipe],
  templateUrl: './anuncios-registrados-sistema-cards.component.html',
  styleUrl: './anuncios-registrados-sistema-cards.component.scss'
})
export class AnunciosRegistradosSistemaCardsComponent {

  @Input() anuncio!: AnuncioRegistradoDTO;

  constructor(private sanitizer: DomSanitizer) { }

  //Metodo que permite obtener la imagen de tipo base64
  getImagenBase64(): SafeUrl | null {
    if (!this.esImagen()) return null;
    if (!this.anuncio.foto) return null;

    const base64 = `data:image/png;base64,${this.anuncio.foto}`;
    return this.sanitizer.bypassSecurityTrustUrl(base64);
  }

  //Metodo que sirve para colocar los videos si end dado caso existen y verificar que la url sea segura
  getVideoUrl(): SafeResourceUrl {
    if (!this.esVideo()) return '';
    if (!this.anuncio.url) return '';


    let url = this.anuncio.url;

    if (url.includes("watch?v=")) {
      const videoId = url.split("watch?v=")[1].split("&")[0];
      url = `https://www.youtube-nocookie.com/embed/${videoId}`;
    } else {
      url = url.replace("youtube.com", "youtube-nocookie.com");
    }

    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  //Metodo que ayuda a saber el tipo de anuncio que es 
  getTipoAnuncio(codigo: number): string {
    switch (codigo) {

      case 1: return TipoAnuncioEnum.ANUNCIO_TEXTO;
      case 2: return TipoAnuncioEnum.IMAGEN_TEXTO;
      case 3: return TipoAnuncioEnum.VIDEO_TEXTO;
      default: return 'Desconocido';
    }

  }


  //Metodos que ayudan a saber que tipo de anuncio es

  //Texto
  esTexto(): boolean {
    return this.anuncio.codigoTipo === 1;
  }

  //Imagen
  esImagen(): boolean {
    return this.anuncio.codigoTipo === 2;
  }

  //Video
  esVideo(): boolean {
    return this.anuncio.codigoTipo === 3;
  }


}
