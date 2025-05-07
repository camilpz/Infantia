import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DaycareRegisterComponent } from './daycare-register.component';

describe('DaycareRegisterComponent', () => {
  let component: DaycareRegisterComponent;
  let fixture: ComponentFixture<DaycareRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DaycareRegisterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DaycareRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
