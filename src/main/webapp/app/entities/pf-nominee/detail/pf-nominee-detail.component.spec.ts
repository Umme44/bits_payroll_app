import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfNomineeDetailComponent } from './pf-nominee-detail.component';

describe('PfNominee Management Detail Component', () => {
  let comp: PfNomineeDetailComponent;
  let fixture: ComponentFixture<PfNomineeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfNomineeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfNominee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfNomineeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfNomineeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfNominee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfNominee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
