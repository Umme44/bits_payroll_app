import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BbbbComponent } from './bbbb.component';

describe('BbbbComponent', () => {
  let component: BbbbComponent;
  let fixture: ComponentFixture<BbbbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BbbbComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BbbbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
