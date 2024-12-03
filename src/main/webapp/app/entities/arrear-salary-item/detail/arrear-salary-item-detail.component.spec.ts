import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArrearSalaryItemDetailComponent } from './arrear-salary-item-detail.component';

describe('ArrearSalaryItem Management Detail Component', () => {
  let comp: ArrearSalaryItemDetailComponent;
  let fixture: ComponentFixture<ArrearSalaryItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrearSalaryItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ arrearSalaryItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArrearSalaryItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArrearSalaryItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load arrearSalaryItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.arrearSalaryItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
