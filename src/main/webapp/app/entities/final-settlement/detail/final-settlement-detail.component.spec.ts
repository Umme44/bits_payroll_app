import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FinalSettlementDetailComponent } from './final-settlement-detail.component';

describe('FinalSettlement Management Detail Component', () => {
  let comp: FinalSettlementDetailComponent;
  let fixture: ComponentFixture<FinalSettlementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinalSettlementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ finalSettlement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FinalSettlementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FinalSettlementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load finalSettlement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.finalSettlement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
