import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HoldFbDisbursementDetailComponent } from './hold-fb-disbursement-detail.component';

describe('HoldFbDisbursement Management Detail Component', () => {
  let comp: HoldFbDisbursementDetailComponent;
  let fixture: ComponentFixture<HoldFbDisbursementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HoldFbDisbursementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ holdFbDisbursement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HoldFbDisbursementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HoldFbDisbursementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load holdFbDisbursement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.holdFbDisbursement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
