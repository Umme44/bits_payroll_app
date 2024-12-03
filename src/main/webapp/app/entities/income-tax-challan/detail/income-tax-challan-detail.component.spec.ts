import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IncomeTaxChallanDetailComponent } from './income-tax-challan-detail.component';

describe('IncomeTaxChallan Management Detail Component', () => {
  let comp: IncomeTaxChallanDetailComponent;
  let fixture: ComponentFixture<IncomeTaxChallanDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IncomeTaxChallanDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ incomeTaxChallan: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IncomeTaxChallanDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IncomeTaxChallanDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load incomeTaxChallan on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.incomeTaxChallan).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
