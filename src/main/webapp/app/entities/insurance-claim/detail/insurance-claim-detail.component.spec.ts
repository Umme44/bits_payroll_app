import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsuranceClaimDetailComponent } from './insurance-claim-detail.component';

describe('InsuranceClaim Management Detail Component', () => {
  let comp: InsuranceClaimDetailComponent;
  let fixture: ComponentFixture<InsuranceClaimDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InsuranceClaimDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ insuranceClaim: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InsuranceClaimDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InsuranceClaimDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load insuranceClaim on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.insuranceClaim).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
