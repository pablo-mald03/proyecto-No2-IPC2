import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearAdminSistemaComponent } from './crear-admin-sistema.component';

describe('CrearAdminSistemaComponent', () => {
  let component: CrearAdminSistemaComponent;
  let fixture: ComponentFixture<CrearAdminSistemaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearAdminSistemaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearAdminSistemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
