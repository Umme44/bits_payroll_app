import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArrearSalaryMasterDetailComponent } from './arrear-salary-master-detail.component';

describe('ArrearSalaryMaster Management Detail Component', () => {
  let comp: ArrearSalaryMasterDetailComponent;
  let fixture: ComponentFixture<ArrearSalaryMasterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrearSalaryMasterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ arrearSalaryMaster: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArrearSalaryMasterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArrearSalaryMasterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load arrearSalaryMaster on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.arrearSalaryMaster).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
