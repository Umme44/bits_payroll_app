import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeductionTypeDetailComponent } from './deduction-type-detail.component';

describe('DeductionType Management Detail Component', () => {
  let comp: DeductionTypeDetailComponent;
  let fixture: ComponentFixture<DeductionTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeductionTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deductionType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeductionTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeductionTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deductionType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deductionType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
