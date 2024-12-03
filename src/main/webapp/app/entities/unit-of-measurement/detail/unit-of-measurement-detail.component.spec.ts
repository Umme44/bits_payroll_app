import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UnitOfMeasurementDetailComponent } from './unit-of-measurement-detail.component';

describe('UnitOfMeasurement Management Detail Component', () => {
  let comp: UnitOfMeasurementDetailComponent;
  let fixture: ComponentFixture<UnitOfMeasurementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UnitOfMeasurementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ unitOfMeasurement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UnitOfMeasurementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UnitOfMeasurementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load unitOfMeasurement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.unitOfMeasurement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
