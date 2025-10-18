import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminsSistemaCardComponent } from './admins-sistema-card.component';

describe('AdminsSistemaCardComponent', () => {
  let component: AdminsSistemaCardComponent;
  let fixture: ComponentFixture<AdminsSistemaCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminsSistemaCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminsSistemaCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
