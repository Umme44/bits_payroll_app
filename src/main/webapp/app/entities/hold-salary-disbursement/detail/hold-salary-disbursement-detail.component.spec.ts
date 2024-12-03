import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HoldSalaryDisbursementDetailComponent } from './hold-salary-disbursement-detail.component';

describe('HoldSalaryDisbursement Management Detail Component', () => {
  let comp: HoldSalaryDisbursementDetailComponent;
  let fixture: ComponentFixture<HoldSalaryDisbursementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HoldSalaryDisbursementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ holdSalaryDisbursement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HoldSalaryDisbursementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HoldSalaryDisbursementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load holdSalaryDisbursement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.holdSalaryDisbursement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
