import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-crear-peliculas-cine',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './crear-peliculas-cine.component.html',
  styleUrl: './crear-peliculas-cine.component.scss'
})
export class CrearPeliculasCineComponent {

peliculaForm: FormGroup;
  posterPreview: string | ArrayBuffer | null = null;
  archivoPoster: File | null = null;

  constructor(
    private fb: FormBuilder,
    //private peliculasService: PeliculasService
  ) {
    this.peliculaForm = this.fb.group({
      nombre: ['', [Validators.required]],
      sinopsis: ['', [Validators.required, Validators.minLength(10)]],
      cast: ['', [Validators.required]],
      director: ['', [Validators.required]],
      fechaEstreno: ['', [Validators.required]],
      clasificacion: ['', [Validators.required]],
      duracion: ['', [Validators.required]],
      precio: [null, [Validators.required, Validators.min(0)]]
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    this.archivoPoster = file;

    // Mostrar vista previa
    const reader = new FileReader();
    reader.onload = () => {
      this.posterPreview = reader.result;
    };
    reader.readAsDataURL(file);
  }

  crearPelicula(): void {
    if (this.peliculaForm.invalid || !this.archivoPoster) {
      console.warn('Formulario inválido o sin poster');
      return;
    }

    const formData = new FormData();

    Object.keys(this.peliculaForm.controls).forEach(key => {
      formData.append(key, this.peliculaForm.get(key)?.value);
    });

    formData.append('poster', this.archivoPoster, this.archivoPoster.name);

   /* this.peliculasService.crearPelicula(formData).subscribe({
      next: (resp) => {
        console.log('🎬 Película creada exitosamente:', resp);
        this.peliculaForm.reset();
        this.posterPreview = null;
        this.archivoPoster = null;
      },
      error: (err) => {
      }
    });*/
  }

  cancelar(): void {
    this.peliculaForm.reset();
    this.posterPreview = null;
    this.archivoPoster = null;
  }

}
